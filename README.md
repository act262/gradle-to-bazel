# Android gradle to bazel

### Test on
AGP: 3.1.+
gradle: 4.6 +
Android: 26

### Feature
- [x] Auto generate bazel build config
- [x] Support maven/local/project dependency
- [x] Support kotlin project
- [x] Support nested project



### TODO
- [ ] Support apt
- [ ] Support Databinding
- [ ] Put all external library in one, and exports to pub, performance need!

### Usage

#### import plugin
Just write into root project's `build.gradle`
```
plugins {
    id 'io.zcx.plugin.bazel'
}

```

#### generate bazel config
execute `genBazel-x` task to generate bazel BUILD


#### bazel run target
```
# only build
bazel build //app

# build and install and startup
bazel mobile-install --start_app //app

# build and install incremental
bazel mobile-install --incremental --start_app //app
```