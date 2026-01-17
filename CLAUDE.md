# CLAUDE.md

## Quick Start

Essential commands for daily development:
```bash
./gradlew bootRun           # Run application

./gradlew test              # Run tests

./gradlew build             # Build project

./gradlew ktlintCheck       # Check code style

./gradlew ktlintFormat      # Format code
```

## Project Info

- **Framework**: Spring Boot 3.3.5 + Kotlin 1.9.25
- **Database**: PostgreSQL + pgvector (H2 for testing)
- **Authentication**: OAuth2 (Google) + JWT
- **Push**: Firebase Admin SDK
- **Testing**: JUnit 5 + MockK + Spring REST Docs
- **Build**: Gradle + Kotlin DSL + Java 17
- **Quality**: ktlint + 4-layer architecture

## Architecture

**4-Layer Structure** (each domain):
- `api/` - Controllers (REST endpoints)
- `app/` - Services & DTOs (business logic)  
- `domain/` - Entities & Repositories (core models)
- `infra/` - External APIs (third-party integrations)

**Domains**: `event`, `interaction`, `push`, `topic`, `user`


### Conventions & Patterns

- **Entity Pattern**: All extend `BaseEntity` with audit fields
- **API Response**: Consistent `ApiResponse<T>` wrapper
- **Naming**: PascalCase (classes), camelCase (methods), snake_case (DB)
- **Code Style**: ktlint defaults, data classes for DTOs