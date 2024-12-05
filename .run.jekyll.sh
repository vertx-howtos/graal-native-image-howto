#!/usr/bin/env bash
set -e

echo "⚙️  Building with Jekyll"
gem install bundler
bundle install
bundle exec jekyll build

echo "✅ Done"
