#!/bin/bash

# Claude Code hook that runs after any file edit operation
# This hook automatically formats and checks Kotlin code using ktlint

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to log messages
log() {
    echo -e "${GREEN}[Claude Hook]${NC} $1"
}

log_info() {
    echo -e "${BLUE}[Claude Hook]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[Claude Hook]${NC} $1"
}

log_error() {
    echo -e "${RED}[Claude Hook]${NC} $1"
}

# Check if the edited file is a Kotlin file
if [[ "$1" == *.kt ]]; then
    log_info "🔧 Kotlin file detected: $1"
    
    # Run ktlint format on the entire project for consistency
    log "📝 Formatting Kotlin code..."
    ./gradlew ktlintFormat --quiet
    
    if [ $? -eq 0 ]; then
        log "✓ Code formatting completed"
        
        # Run ktlint check on the entire project
        log "🔍 Running code style checks..."
        ./gradlew ktlintCheck --quiet
        
        if [ $? -eq 0 ]; then
            log "✅ All code style checks passed"
        else
            log_error "❌ Code style issues found"
            log_warn "Please review ktlint output above"
            exit 1
        fi
    else
        log_error "❌ Code formatting failed"
        log_warn "Please check gradle configuration"
        exit 1
    fi
else
    log_info "📄 Non-Kotlin file edited, skipping ktlint checks"
fi

log_info "🏁 Hook completed successfully"