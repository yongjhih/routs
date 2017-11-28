[![JitPack](https://img.shields.io/github/tag/yongjhih/routs.svg?label=JitPack)](https://jitpack.io/#yongjhih/routs)
[![javadoc](https://img.shields.io/github/tag/yongjhih/routs.svg?label=javadoc)](https://jitpack.io/com/github/yongjhih/routs/-SNAPSHOT/javadoc/)
[![codecov](https://codecov.io/gh/yongjhih/routs/branch/master/graph/badge.svg)](https://codecov.io/gh/yongjhih/routs)
[![CircleCI](https://circleci.com/gh/yongjhih/routs/tree/master.svg?style=svg)](https://circleci.com/gh/yongjhih/routs)

# routs

Simple Router

## Usage

```java
PathMatcher matcher = PathMatcher()
matcher.add("home/12345678-1234-4321-9876-123456789012/u/123456789012/v1");
matcher.add("home/<([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89ABab][0-9a-fA-F]{3}-[0-9a-fA-F]{12})>/u/<([a-fA-F0-9]{12})>/v1")
matcher.add("home/:uuid<([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89ABab][0-9a-fA-F]{3}-[0-9a-fA-F]{12})>/u/:uid<([a-fA-F0-9]{12})>/v2")

matcher.matches("home/12345678-1234-4321-9876-123456789012/u/123456789012/v1");
matcher.matches("home/ffffffff-ffff-4fff-afff-ffffffffffff/u/ffffffffffff/v1");
matcher.matches("home/ffffffff-ffff-4fff-afff-ffffffffffff/u/ffffffffffff/v2");
assertThat(matcher.namedPath.get("uuid")).isEqualTo("ffffffff-ffff-4fff-afff-ffffffffffff");
assertThat(matcher.namedPath.get("uid")).isEqualTo("ffffffffffff");
```

## Installation

```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.yongjhih:routs:0.0.2'
}
```
