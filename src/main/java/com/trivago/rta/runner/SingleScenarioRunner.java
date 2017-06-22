/*
 * Copyright 2017 trivago GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trivago.rta.runner;

import com.trivago.rta.exceptions.MissingFileException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

/**
 * Represents a test runner for a single scenario.
 */
public final class SingleScenarioRunner {
    /**
     * Template variable that needs to be replaced by the
     * real feature file name inside the runner template.
     */
    private static final String FEATURE_PLACEHOLDER = "[FEATURE_FILE_NAME]";

    /**
     * The path to the runner file template.
     */
    private final String runnerTemplateLocation;

    /**
     * The name of the feature file this runner belongs to.
     */
    private final String featureFile;

    /**
     * Constructor for a single scenario runner.
     *
     * @param runnerTemplatePath The path to the runner template.
     * @param featureFileName    The name of the feature file for this runner.
     */
    public SingleScenarioRunner(
            final String runnerTemplatePath,
            final String featureFileName) {
        this.runnerTemplateLocation = runnerTemplatePath;
        this.featureFile = featureFileName;
    }

    /**
     * Returns the full content for the concrete runner file.
     *
     * @return The file content for the runner file.
     * @throws MissingFileException Thrown if the runner file is missing.
     */
    public String getRenderedRunnerFileContent() throws MissingFileException {
        String fileString = null;
        try {
            fileString = new String(
                    Files.readAllBytes(
                            Paths.get(runnerTemplateLocation)),
                    StandardCharsets.UTF_8);
            fileString = fileString.replace(FEATURE_PLACEHOLDER, featureFile);
            fileString = addCucableComment(fileString);
        } catch (IOException e) {
            throw new MissingFileException(runnerTemplateLocation);
        }
        return fileString;
    }

    /**
     * Adds the "Generated by Cucable" message to a string.
     *
     * @param source The source string.
     * @return The new string with the Cucable message attached.
     */
    private String addCucableComment(final String source) {
        return source.concat(System.lineSeparator()
                + "// Generated by Cucable, "
                + new Date() + System.lineSeparator());
    }

    @Override
    public String toString() {
        return "SingleScenarioRunner{"
                + "runnerTemplateLocation='" + runnerTemplateLocation + '\''
                + ", featureFile='" + featureFile + '\''
                + '}';
    }
}