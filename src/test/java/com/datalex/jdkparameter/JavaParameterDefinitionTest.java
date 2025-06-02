package com.datalex.jdkparameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;

import hudson.model.JDK;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class JavaParameterDefinitionTest {

    private static final JDK JDK_11 = new JDK("jdk-11", "/my/jdk/11");
    private static final JDK JDK_17 = new JDK("jdk-17", "/my/jdk/17");
    private static final JDK JDK_21 = new JDK("jdk-21", "/my/jdk/21");
    private static final JDK CURRENT = new JDK("current-jdk", System.getProperty("java.home"));

    private JenkinsRule j;

    @BeforeEach
    void setUp(JenkinsRule rule) {
        j = rule;
        j.jenkins.setJDKs(List.of(JDK_11, JDK_17, JDK_21, CURRENT));
    }

    @Test
    void testGetJDKNames() {
        assertThat(
                JavaParameterDefinition.getJDKNames(),
                containsInAnyOrder(
                        JDK_11.getName(),
                        JDK_17.getName(),
                        JDK_21.getName(),
                        CURRENT.getName(),
                        JavaParameterDefinition.ALL_JDK,
                        JavaParameterDefinition.DEFAULT_JDK));
    }

    @Test
    void testGetJDKNamesDefault() {
        assertThat(
                JavaParameterDefinition.getJDKNamesDefault(),
                containsInAnyOrder(
                        JDK_11.getName(),
                        JDK_17.getName(),
                        JDK_21.getName(),
                        CURRENT.getName(),
                        JavaParameterDefinition.DEFAULT_JDK));
    }

    @Test
    void testGetJDKSasStrings() {
        assertThat(
                JavaParameterDefinition.getJDKSasStrings(),
                containsInAnyOrder(JDK_11.getName(), JDK_17.getName(), JDK_21.getName(), CURRENT.getName()));
    }

    @Test
    void testGetAllowedJDKs() {
        List<String> allowedJDKs = List.of(JDK_11.getName());
        JavaParameterDefinition param =
                new JavaParameterDefinition(JDK_11.getName(), "JDK 11", CURRENT.getName(), allowedJDKs);
        assertThat(param.getAllowedJDKs(), containsInAnyOrder(JDK_11.getName()));
    }

    @Test
    void testGetAllowedJDKsWithAllJDKs() {
        List<String> allowedJDKs = List.of(JDK_17.getName(), JavaParameterDefinition.ALL_JDK);
        JavaParameterDefinition param =
                new JavaParameterDefinition(JDK_11.getName(), "JDK 11", CURRENT.getName(), allowedJDKs);
        assertThat(param.getAllowedJDKs(), hasItems(JavaParameterDefinition.ALL_JDK));
    }
}
