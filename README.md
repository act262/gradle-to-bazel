# Android gradle to bazel

[![Download](https://api.bintray.com/packages/act262/maven/bazel/images/download.svg)](https://bintray.com/act262/maven/bazel/_latestVersion)

### Bazel for Android

[Bazel](https://www.bazel.build)


### Feature
- [x] Auto generate bazel build config
- [x] Support maven/local/project dependency
- [x] Support kotlin project
- [x] Support nested project


### TODO
- [ ] Put all external library in one, and exports to pub, performance need!
- [ ] More ...

#### LIMIT
- when use kotlin, bazel not support apt
- bazel not support Databinding v2+
- some jar desugar error by bazel
- other unknow bazel limit...


#### Test on
AGP: 3.1.+
gradle: 4.4 +
Android: 26

---

### Usage

#### Using the plugins DSL:
Just write into root project's `build.gradle`
```
plugins {
    id 'io.zcx.plugin.bazel' version $latest
}
```
*Must before other config*

Or
#### Using legacy plugin application:
```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "io.zcx.plugin:plugin:$latest"
  }
}

apply plugin: "io.zcx.plugin.bazel"
```


#### Execute generate task
Sync project, and execute `genBazel-x` task to generate bazel BUILD


#### Run bazel
```
# only build
bazel build //app

# build and install and startup
bazel mobile-install --start_app //app

# build and install incremental
bazel mobile-install --incremental --start_app //app
```


### Ref
[android-rules](https://docs.bazel.build/versions/master/be/android.html#android-rules)

[tutorial](https://docs.bazel.build/versions/master/tutorial/android-app.html)

[mobile-install](https://docs.bazel.build/versions/master/mobile-install.html)