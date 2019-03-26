#/usr/bin/env bash
set -e

bundle install
bundle exec jekyll build
