# Database Configuration Guide

## PostgreSQL Setup (AWS RDS)

This application uses PostgreSQL database hosted on AWS RDS.

### Connection Details

- **Host:** `arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com`
- **Port:** `5432`
- **Username:** `arcuser`
- **Password:** `qn31f0nn0S`
- **Database Name:** Set via `DB_NAME` environment variable

### Environment Variables

The application uses environment variables for secure configuration. Set the following variables:

#### Required Variables

```bash
# Database Configuration
DB_HOST=arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com
DB_PORT=5432
DB_NAME=your_database_name
DB_USERNAME=arcuser
DB_PASSWORD=qn31f0nn0S
```

#### Optional Variables

```bash
# JWT Configuration (if not set in application.yml)
JWT_SECRET=your-secret-key
JWT_EXPIRATION_MS=86400000

# Spring Profile
SPRING_PROFILES_ACTIVE=dev
```

### Setting Environment Variables

#### Windows (PowerShell)

```powershell
$env:DB_HOST="arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com"
$env:DB_PORT="5432"
$env:DB_NAME="shophub_dev"
$env:DB_USERNAME="arcuser"
$env:DB_PASSWORD="qn31f0nn0S"
```

#### Windows (Command Prompt)

```cmd
set DB_HOST=arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com
set DB_PORT=5432
set DB_NAME=shophub_dev
set DB_USERNAME=arcuser
set DB_PASSWORD=qn31f0nn0S
```

#### Linux/macOS

```bash
export DB_HOST=arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com
export DB_PORT=5432
export DB_NAME=shophub_dev
export DB_USERNAME=arcuser
export DB_PASSWORD=qn31f0nn0S
```

### Using .env File (Recommended)

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Update `.env` with your actual values

3. **Note:** Spring Boot doesn't natively support `.env` files. You can use:
   - IDE plugins (IntelliJ, VS Code)
   - Tools like `dotenv-java` library
   - Or set environment variables directly

### Database Connection URL Format

```
postgresql://arcuser:qn31f0nn0S@arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com:5432/<dbName>
```

### Profiles

- **Development (`dev`):** Uses `application-dev.yml`
  - `ddl-auto: update` - Automatically updates schema
  - `show-sql: true` - Shows SQL queries in logs
  
- **Production (`prod`):** Uses `application-prod.yml`
  - `ddl-auto: validate` - Only validates schema, doesn't modify
  - `show-sql: false` - Hides SQL queries in logs
  - Larger connection pool

### Running the Application

1. Set environment variables (see above)
2. Ensure the database exists on AWS RDS
3. Run the application:
   ```bash
   mvnw.cmd spring-boot:run
   ```

### Troubleshooting

#### Connection Refused
- Verify AWS RDS instance is running
- Check security group allows connections from your IP
- Verify host and port are correct

#### Authentication Failed
- Verify `DB_USERNAME` and `DB_PASSWORD` are correct
- Check if user has proper permissions

#### Database Does Not Exist
- Create the database on AWS RDS first
- Update `DB_NAME` environment variable

#### SSL Connection Issues
If you encounter SSL errors, you may need to add SSL parameters to the connection URL:
```
jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}?sslmode=require
```

### Security Best Practices

1. ✅ **Never commit `.env` file** - Add to `.gitignore`
2. ✅ **Use environment variables** - Don't hardcode credentials
3. ✅ **Rotate passwords regularly** - Update `DB_PASSWORD` periodically
4. ✅ **Use different databases** - Separate dev/staging/prod databases
5. ✅ **Restrict access** - Use AWS security groups to limit access
