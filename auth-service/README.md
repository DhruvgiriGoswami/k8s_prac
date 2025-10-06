## Auth Service

Spring Boot microservice for user management and JWT issuance.

### Features
- User registration, update, list
- Secure password hashing (BCrypt) with password policy
- JWT login with stateless security and authorization headers
- Layered architecture (controller, service, repository, domain)
- Global exception handling and CORS filter
- Tests (unit + integration)

### Security hardening
- CSRF disabled (stateless APIs); strict headers (XSS, CSP, HSTS, frame options)
- JWT validation via filter; authenticated endpoints require Bearer token
- Registration and login are public; others require auth
- ORM via Spring Data JPA prevents SQL injection (no string concatenation)
- Validation on inputs; password policy enforced server-side

### Build (Jar) and Local Run
```bash
mvn clean package
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

Environment variables:
- `DB_URL` (default `jdbc:postgresql://postgres:5432/authdb`)
- `DB_USERNAME` (default `authuser`)
- `DB_PASSWORD` (default `authpass`)
- `JWT_SECRET` (Base64 key, default provided for dev only)
- `JWT_EXPIRATION_SECONDS` (default `3600`)

### Container Image Build
You deploy a container image to Kubernetes, not the raw jar file.

Option A: Build image with Spring Boot buildpacks
```bash
mvn -DskipTests spring-boot:build-image -Dspring-boot.build-image.imageName=auth-service:0.0.1-SNAPSHOT
```

Option B: Build with Dockerfile (create one if preferred)
```bash
docker build -t auth-service:0.0.1-SNAPSHOT .
```

Push to your registry (example):
```bash
docker tag auth-service:0.0.1-SNAPSHOT <registry>/auth-service:0.0.1-SNAPSHOT
docker push <registry>/auth-service:0.0.1-SNAPSHOT
```

Update `image:` in `k8s/auth-deployment.yaml` to your registry path.

### Deploy to Kubernetes
1) Ensure Postgres and Keycloak are up (if needed):
```bash
kubectl apply -f ../keycloak-service/postgres-deployment.yaml
kubectl apply -f ../keycloak-service/keycloak-deployment.yaml
```

2) Apply auth service manifests:
```bash
kubectl apply -f k8s/auth-deployment.yaml
```

3) Verify:
```bash
kubectl get pods,svc
```

### API
- POST `/api/users` register
- PUT `/api/users/{id}` update
- GET `/api/users` list
- POST `/api/auth/login` login -> `{ token }`


