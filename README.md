# Database Migration using Spring Batch & Spring Boot
## Databases:
- Source DB: PostgreSQL 
  - student Table
  - subjects_learning Table
- Destination Migration DB: MySQL
  - student Table
  - subjects_learning Table
- Spring Batch Metadata DB: MySQL
- Migration order matters 
  - student is parent table
  - subjects_learning has FK tagged to parent table
  - hence student table needs to be migrated first 
  - if not will have error when migrating subjects_learning
- Fill application.properties with your own db credentials

## End Points:
- /api/v1/job/start/student -> run student migration job
- /api/v1/job/start/subject -> run subject migration job
  -  request body (optional): pass in currSubjCount & maxSubjCount in json to set the number of records to be read in the job
- /api/v1/job/stop/{execution id} -> stop existing running job

