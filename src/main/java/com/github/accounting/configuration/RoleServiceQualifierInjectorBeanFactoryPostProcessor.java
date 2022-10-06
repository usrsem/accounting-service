package com.github.accounting.configuration;

import com.github.accounting.service.RoleServiceQualifier;
import com.github.accounting.service.impl.ChainOfResponsibilityRoleServiceQualifier;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;

@Component
public class RoleServiceQualifierInjectorBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (var beanName : beanFactory.getBeanDefinitionNames()) {
            tryProcessBeanDefinition(beanFactory.getBeanDefinition(beanName), beanFactory);
        }
    }

    private void tryProcessBeanDefinition(BeanDefinition beanDefinition, ConfigurableListableBeanFactory beanFactory) {
        try {
            processBeanDefinition(beanDefinition, beanFactory);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void processBeanDefinition(
            BeanDefinition beanDefinition,
            ConfigurableListableBeanFactory beanFactory
    ) throws ClassNotFoundException {
        var beanClassName = beanDefinition.getBeanClassName();
        if (beanClassName != null) {
            registerBeanDefinitions(beanClassName, beanFactory);
        }
    }

    private void registerBeanDefinitions(
            String beanClassName,
            ConfigurableListableBeanFactory beanFactory
    ) throws ClassNotFoundException {
        var declaredFields = getBeanDeclaredFields(beanClassName);
        var registry = (BeanDefinitionRegistry) beanFactory;

        for (var field : declaredFields) {
            if (field.getType().equals(RoleServiceQualifier.class)) {
                var genericClass = getGenericClassFromField(field);
                var newBeanName = createBeanName(genericClass);
                var servicesBeanName = newBeanName + "ServicesMap";
                registry.registerBeanDefinition(servicesBeanName, createServicesBeanDefinition(
                        beanFactory,
                        genericClass
                ));
                registry.registerBeanDefinition(newBeanName, createRoleServiceQualifierBeanDefinition(
                        newBeanName,
                        genericClass
                ));
            }
        }
    }

    private Field[] getBeanDeclaredFields(String beanClassName) throws ClassNotFoundException {
        Class<?> beanClass = Class.forName(beanClassName);
        return beanClass.getDeclaredFields();

    }

    private Class<?> getGenericClassFromField(Field field) throws ClassNotFoundException {
        ResolvableType type = ResolvableType.forField(field);
        var genericType = type.getGenerics()[0].getType();
        return Class.forName(genericType.getTypeName());
    }

    private String createBeanName(Class<?> genericClass) {
        var toRemove = RoleServiceQualifier.class.getSimpleName();
        var prefix = genericClass.getSimpleName().replace(toRemove, "");
        return prefix.substring(0, 1).toLowerCase()
                + prefix.substring(1)
                + "Qualifier";
    }

    private BeanDefinition createServicesBeanDefinition(
            ConfigurableListableBeanFactory beanFactory,
            Class<?> genericClass
    ) {
        return BeanDefinitionBuilder
                .genericBeanDefinition(HashMap.class, () -> {
                    var map = new HashMap<String, Object>();
                    var beanNamesForType = beanFactory.getBeanNamesForType(genericClass);
                    for (var name : beanNamesForType) {
                        map.put(name, beanFactory.getBean(name));
                    }
                    return map;
                })
                .getBeanDefinition();
    }

    private BeanDefinition createRoleServiceQualifierBeanDefinition(String beanName, Class<?> genericClass) {
        return BeanDefinitionBuilder
                .genericBeanDefinition(ChainOfResponsibilityRoleServiceQualifier.class)
                .addConstructorArgReference(beanName + "ServicesMap")
                .addConstructorArgValue(genericClass.getSimpleName())
                .getBeanDefinition();
    }
}
