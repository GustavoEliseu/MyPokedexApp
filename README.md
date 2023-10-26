# Complete Pokemon Pokedex <br>

This is a personal android app project that utilizes Apollo with the GraphQL pokeApi to fill a pokedex with Palette to get the pokemon colors. I'm dividing this project in 3 parts:

<p align="center">
  <img src="https://github.com/GustavoEliseu/MyPokedexApp/assets/30469845/47b093bf-3540-46af-8b8f-3d0af269cefd"/><br>
  <em>Screenshot of the initial version</em>
</p>


## Part 1 - Pokedex <br>
#### Testing features that i never used so expect a messy code on this part <br>
* Implement the basic pokedex feature: <br>
  * Search - **${\color{green}DONE}$** <br>
  * List - **${\color{green}DONE}$** (need small layout fixes) <br>
  * Use Android Palette to get the pokemon color - ****${\color{green}DONE}$**** <br>
  * PokemonDetails Screen - **${\color{yellow}TODO}$** <br>
  * AbilityDetails Screen - **${\color{yellow}TODO}$** <br>
  * MoveDetails Screen - **${\color{yellow}TODO}$** <br>
* Add test libraries and start the testing - **${\color{yellow}TODO}$** <br>
* Cleanup and improve git Readme layout - **${\color{yellow}TODO}$** <br>
* Add and configure apolloCache - **${\color{green}DONE}$**<br> (Still needs to add local search, going to wait for the ROOM database)

## Part 2 - Offline first and improvements <br>
* Adds error messages for communication errors - **${\color{yellow}TODO}$**<br>
* Fixes Architecture flaws - **${\color{yellow}TODO}$**<br>
* Add Room database - **${\color{yellow}PARTIALLY DONE}$** Add tables for Ability, Moves, Details etc...<br>
* if possible planning to add an option to download the data locally, with option to choose to download or not the images - **${\color{yellow}TODO}$**<br>
* Check for accessibility, text-to-speak, manual tests with content readers, test colors for better contrast etc... - **${\color{yellow}TODO}$**<br>

## Part 3 - Mount your own team <br>
* List with pokemons to choose - **${\color{yellow}TODO}$**<br>
* Choose skills and moves - **${\color{yellow}TODO}$**<br>
* Check type coverage - **${\color{yellow}TODO}$** <br>
* Check move type coverage - **${\color{yellow}TODO}$**<br>

## Bonus - Better version of mount your team<br>
* Option to filter for competitive tiers (Uber, OU, BL etc) - **${\color{yellow}TODO}$** (Needs an api with up to date data, maybe Smogon or PokemonShowdown) <br>
* Add the filter above for pokemons, skills, moves and items - **${\color{yellow}TODO}$**<br>
* 100% test coverage - **${\color{yellow}TODO}$**<br>


## major changes:
* Removed retrofit and added Apollo - **${\color{green}DONE}$**
* Started using PokeApi Graphql - **${\color{green}DONE}$**
* Added localHost build variant - **${\color{green}DONE}$**

Credits - **${\color{yellow}TODO}$**<br> ( add all libraries and people that helped)
