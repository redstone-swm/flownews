# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

**Build the project:**
```bash
./gradlew build
```

**Run the application:**
```bash
./gradlew bootRun
```

**Run tests:**
```bash
./gradlew test
```

**Run code linting (ktlint):**
```bash
./gradlew ktlintCheck
```

**Format code:**
```bash
./gradlew ktlintFormat
```

**Run a single test class:**
```bash
./gradlew test --tests "ClassName"
```

## Architecture Overview

This is a Spring Boot application written in Kotlin that provides a news aggregation and topic tracking system called FlowNews.

### Domain Architecture

The application follows a layered architecture with clear separation of concerns:

- **API Layer** (`api/*/api/`): REST controllers handling HTTP requests
- **Application Layer** (`api/*/app/`): Service classes containing business logic
- **Domain Layer** (`api/*/domain/`): Entity classes and repositories
- **Infrastructure Layer** (`api/*/infra/`): External integrations and technical details

### Core Domain Models

**User Management:**
- `User` entity supports OAuth2 authentication (Google) with JWT tokens
- Users have roles, optional birth date and gender fields
- Device tokens for push notifications

**Content System:**
- `Topic`: Main content containers with title, description, and image
- `Event`: Time-ordered events within topics with articles
- `Article`: News articles linked to events
- Relationships: Topic → Events → Articles (one-to-many hierarchical)

**Features:**
- Topic subscriptions with push notifications
- User event logging and history tracking
- OAuth2 + JWT authentication system

### Key Technologies

- **Framework**: Spring Boot 3.5 with Kotlin 1.9.25
- **Database**: PostgreSQL with JPA/Hibernate
- **Authentication**: OAuth2 (Google) + JWT
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Push Notifications**: Firebase Admin SDK
- **Code Quality**: ktlint for formatting

### Database Configuration

- Uses PostgreSQL in production
- H2 for testing
- JPA auditing enabled with `BaseEntity` for created/modified timestamps
- DDL auto-generation set to `create-drop` (development mode)

### Security

- All endpoints currently permit all requests (development configuration)
- OAuth2 login redirects to frontend with JWT token
- JWT authentication filter for API requests
- Stateless session management

### Environment Variables Required

- `POSTGRESQL_URI`, `DB_USERNAME`, `DB_PASSWORD`: Database connection
- `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`: OAuth2 configuration
- `JWT_SECRET`: JWT token signing
- `FRONTEND_URL`: Frontend application URL
- `ALLOWED_ORIGINS`: CORS configuration