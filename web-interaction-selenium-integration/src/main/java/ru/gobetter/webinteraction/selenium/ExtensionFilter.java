package ru.gobetter.webinteraction.selenium;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileFilter;

@RequiredArgsConstructor
class ExtensionFilter implements FileFilter {
    private final String extension;

    @Override
    public boolean accept(File pathname) {
        return pathname.getName().endsWith(extension);
    }
}
