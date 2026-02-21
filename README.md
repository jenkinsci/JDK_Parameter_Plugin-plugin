# JDK Parameter Plugin

Adds a build parameter that allows selecting a configured JDK installation for each build.

## Description

This plugin adds a build parameter that allows setting which JDK should be used for a job on a per-build basis.

The JDKs available for selection can be configured on the job configuration page when the JDK parameter is enabled.

## Configuration

1. Go to **Manage Jenkins → Global Tool Configuration**
2. Configure one or more JDK installations
3. Save the configuration

To use in a job:

1. Open the job configuration page
2. Enable **This build is parameterized**
3. Add parameter → Select **JDK Parameter**
4. Choose the default JDK and allowed JDK selections

## Usage

On the build parameters page, a dropdown list allows selecting which configured JDK should be used for that build.

## Pipeline Usage Example

```groovy
pipeline {
    agent any
    parameters {
        jdk(name: 'JDK17')
    }
    stages {
        stage('Check Java Version') {
            steps {
                sh 'java -version'
            }
        }
    }
}