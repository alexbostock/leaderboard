# leaderboard

A leaderboard system with a RESTful API

## Usage

Build and run using Maven:

```
mvn verify
mvn exec:java
```

## Interface

- `GET /scores`: returns all recorded scores.
- `POST /scores`: takes two arguments (`nickname: string` and `score: uint`) and stores that score.