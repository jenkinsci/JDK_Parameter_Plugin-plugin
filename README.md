# JDK Parameter Plugin

Adds a build parameter that allows selecting a configured JDK installation for each build.

---

## Description

This plugin adds a build parameter that allows setting which JDK should be used for a job on a per-build basis.

The JDKs available for selection can be configured on the job configuration page when the JDK parameter is enabled.

---

## Installation

1. Go to **Manage Jenkins → Manage Plugins**
2. Search for **JDK Parameter Plugin**
3. Install the plugin
4. Restart Jenkins if required

---

## Configuration

1. Go to **Manage Jenkins → Global Tool Configuration**
2. Configure one or more JDK installations
3. Save the configuration

To use in a job:

1. Open the job configuration page
2. Enable **This build is parameterized**
3. Add parameter → Select **JDK Parameter**
4. Choose the default JDK and allowed JDK selections

---

## Usage

On the build parameters page, a dropdown list allows selecting which configured JDK should be used for that build.

---

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
```

---

## Known Issues

During the build phase of a job that has JDK Parameter configured, the job's configured **JDK option** is temporarily replaced with the selected JDK Parameter value and restored after the build.

If multiple builds run concurrently, the original JDK configuration may not always be restored correctly.

---

## Compatibility

- Requires Jenkins 2.479.1 or newer

---

## Changelog

### Version 1.0
- Initial release