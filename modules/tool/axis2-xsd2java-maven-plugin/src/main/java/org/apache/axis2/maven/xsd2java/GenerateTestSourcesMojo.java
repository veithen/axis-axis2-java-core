/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.axis2.maven.xsd2java;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Generates Java classes from the specified schema files, for use in unit tests. This goal binds by
 * default to the generate-test-sources phase and adds the sources to the test sources of the
 * project; it is otherwise identical to the axis2-xsd2java:generate-sources goal.
 */
@Mojo(name = "generate-test-sources", defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES, threadSafe = true)
public class GenerateTestSourcesMojo extends AbstractXSD2JavaMojo {
    /**
     * The output directory for the generated Java code.
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-test-sources/xsd2java")
    private File outputDirectory;
    
    @Parameter(property = "maven.test.skip")
    private boolean skip;
    
    @Override
    protected File getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    protected void addSourceRoot(MavenProject project) {
        project.addTestCompileSourceRoot(outputDirectory.getPath());
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("Not generating test sources");
        } else {
            super.execute();
        }
    }
}
