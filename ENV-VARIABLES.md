# Environment Variables Configuration

## Required Environment Variables

Copy these variables and set them in your environment before running the application:

```bash
# Database Configuration (AWS RDS PostgreSQL)
DB_HOST=arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com
DB_PORT=5432
DB_NAME=shophub_dev
DB_USERNAME=arcuser
DB_PASSWORD=qn31f0nn0S
```

## Setting Environment Variables

### Windows PowerShell
```powershell
$env:DB_HOST="arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com"
$env:DB_PORT="5432"
$env:DB_NAME="shophub_dev"
$env:DB_USERNAME="arcuser"
$env:DB_PASSWORD="qn31f0nn0S"
```

### Windows Command Prompt
```cmd
set DB_HOST=arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com
set DB_PORT=5432
set DB_NAME=shophub_dev
set DB_USERNAME=arcuser
set DB_PASSWORD=qn31f0nn0S
```

### Linux/macOS
```bash
export DB_HOST=arc-cwa-apne1-cwa-intern-test-1.cluster-cym1l22kuc0z.ap-northeast-1.rds.amazonaws.com
export DB_PORT=5432
export DB_NAME=shophub_dev
export DB_USERNAME=arcuser
export DB_PASSWORD=qn31f0nn0S
```

## Notes

- Default values are provided in `application-dev.yml` for development
- For production, always use environment variables
- Never commit actual credentials to version control
