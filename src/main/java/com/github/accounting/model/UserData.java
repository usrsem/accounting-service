package com.github.accounting.model;

import java.util.Collection;

public record UserData(long id, String name, Collection<String> authorities) {
}
