# leaderboard

A leaderboard system with a RESTful API

## Usage

Build and run using Maven:

```
mvn verify
mvn exec:java
```

Test using Maven:

```
mvn test
```

## Interface

- `GET /scores`: returns all recorded scores.
- `GET /scores/top/<n>`: returns the top `n` scores (`n: uint`).
- `POST /scores`: takes two arguments (`nickname: string` and `score: uint`) and stores that score.