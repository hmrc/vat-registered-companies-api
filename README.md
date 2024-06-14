
# vat-registered-companies-api

This is the vat-registered-companies-api repo.

It has been upgraded to Java 11, sbt 1.9.9, Scala 2.13.12, Play 3.0.

# Run Services

You can run services locally through Service Manager:
```
sm2 --start VAT_REG_CO_ALL
```

To run repo on port 8733:
```
sbt run
```

## Testing and coverage

To run tests use:
```
sbt test
```

To run tests and coverage use:
```
sbt clean coverage test coverageReport
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
