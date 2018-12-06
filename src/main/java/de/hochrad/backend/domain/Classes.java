package de.hochrad.backend.domain;

import java.util.List;

public class Classes {

    private List<String> classes;

    private Classes() {

    }

    public Classes(List<String> classes) {
        this.classes = classes;
    }

    public List<String> getClasses() {
        return classes;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Classes) {
            return ((Classes) o).getClasses().equals(getClasses());
        }
        return false;
    }
}
