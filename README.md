# LokalDeals

## Testing

The project includes a dedicated test profile that runs against an in-memory H2 database.

- Run tests with:

```bash
cd LokalDeals/LokalDeals
./mvnw test
```

This avoids requiring a local MySQL server during unit test execution.

## Development

The default runtime configuration in `src/main/resources/application.properties` is configured for MySQL. If you are running the application locally, make sure to update the MySQL credentials and ensure the database is accessible.
