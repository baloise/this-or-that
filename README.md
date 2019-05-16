# this-or-that
A template to use when starting a new project.

## perform a repository wide search and replace for "this-or-that" and the "target-repo-name"

e.g. by using

```
cp -R this-or-that/ new-name && cd new-name && git config --local --unset remote.origin.url && git config --local --add remote.origin.url git@github.com:baloise/new-name.git && git reset --hard $(git commit-tree FETCH_HEAD^{tree} -m "Initial contribution") &&  git grep -l 'this-or-that' | xargs sed -i '' -e 's/this-or-that/new-name/g' && mvn clean verify && git add -A && git commit -m "Rename from template to new-name" && cd ..
```

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bf6fa237dd934970991ecba2c66db23e)](https://app.codacy.com/app/baloise/this-or-that?utm_source=github.com&utm_medium=referral&utm_content=baloise/this-or-that&utm_campaign=Badge_Grade_Dashboard)
[![DepShield Badge](https://depshield.sonatype.org/badges/baloise/this-or-that/depshield.svg)](https://depshield.github.io)
[![Build Status](https://travis-ci.org/baloise/this-or-that.svg?branch=master)](https://travis-ci.org/baloise/this-or-that)
[![Codecov](https://img.shields.io/codecov/c/github/baloise/this-or-that.svg)](https://codecov.io/gh/baloise/this-or-that)
[![gitpod-IDE](https://img.shields.io/badge/open--IDE-as--gitpod-blue.svg?style=flat&label=openIDE)](https://gitpod.io#https://github.com/baloise/this-or-that)

## the [docs](docs/index.md)
