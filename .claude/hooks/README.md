# Claude Code Hooks

This directory contains hooks that automatically run when Claude Code performs certain operations.

## Available Hooks

### post-edit.sh
- **Trigger**: Runs after any file edit operation
- **Function**: Automatically formats and checks Kotlin code using ktlint
- **Behavior**:
  - Detects if edited file is a Kotlin file (*.kt)
  - Runs `./gradlew ktlintFormat` to auto-format entire project
  - Runs `./gradlew ktlintCheck` to verify code style compliance
  - Provides colored output with emojis for clear status indication
  - Exits with error code if style issues are found
  - Ensures immediate feedback on code quality

## Hook Configuration

To enable these hooks in Claude Code, ensure:
1. Hooks are executable (`chmod +x .claude/hooks/*.sh`)
2. Gradle wrapper is available in project root
3. ktlint plugin is configured in build.gradle.kts