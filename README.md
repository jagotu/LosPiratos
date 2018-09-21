# Los Piratos de la Casa

An interactive strategic computer&board game, settled in the famous world of 17th century's Caribbean pirates.

## Getting Started

Java 8 is required, both Windows and Linux are supported. To run, use maven:

```mvn package```
```cd target```
```java -jar LosPiratos-2018.1-jar-with-dependencies.jar```

The project uses JavaFX which is a standard part of Java 8. Some Ubuntu users may have Java 8 without JavaFX; to fix, install it with ``sudo apt-get install openjfx``.

## Gameplay

The game is meant to be played by 3 - 6 teams of 1 - 6 players, who are all in the same room, watching the main game view on a big screen / projector.

Intended game rules are available in the file [pravidla - Los Piratos de la Casa.pdf](pravidla%20-%20Los%20Piratos%20de%20la%20Casa.pdf), but only in Czech. Sorry about that. However, you can play around without knowing the exact rules, the UI is pretty expressive.

### Localisation

The game text is in Czech, yet most of the UI is graphical and thus international. You can translate the game by adding a new method to the Translatable interface. In the source code, all names are in English and are expressive, thus translating them should be easy.

## Versioning

Version number is in format ``YYYY.n`` where ``YYYY`` is deployment year and ``n`` is minor revision. This is convenient for the way the authors use and update the game.

## Built With

* [Java](https://www.java.com/en/)
* [JavaFX](http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html) - The Rich Client Platform
* [Maven](https://maven.apache.org/) - Dependency Management
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) - IDE, incl. Dependency Management

## Contributing

Feel free to add pull requests or open new Issues.

## Authors

* **jagotu** - *UI* - [Github/jagotu](https://github.com/jagotu)
* **Antonin Teichmann** - *game backend* - [Github/teichmaa](https://github.com/teichmaa)

## Credits

Special thanks to:

* **Petr Martišek** ([Github/martisekpetr](https://github.com/martisekpetr)) and **Marek Basovník** for inventing and designing the board game
* **Adam Hornáček** ([Github/Orviss](https://github.com/Orviss)) for his support and feedback regarding our code

## License

See [LICENSE](LICENSE) and [THIRD-PARTY.txt](THIRD-PARTY.txt) for more details.
