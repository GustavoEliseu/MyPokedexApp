# Complete Pokemon Pokedex <br>

This is a personal android app project that utilizes Apollo with the GraphQL pokeApi to fill a pokedex with Palette to get the pokemon colors. I'm dividing this project in 3 parts:

<p align="center">
  <img src="https://github.com/GustavoEliseu/MyPokedexApp/assets/30469845/47b093bf-3540-46af-8b8f-3d0af269cefd" /img><br>
  <em>Screenshot of the initial version</em>
</p>

## Part 1 - Pokedex <br>
#### Testing features that i never used so expect a messy code on this part <br>
* Implement the basic pokedex feature: <br>
  * Search - **TODO** (partially done, needs layout and small fixes) <br>
  * List - **Done** (need small layout fixes) <br>
  * Use Android Palette to get the pokemon color - **Done** <br>
  * PokemonDetails Screen - **TODO** (Redo the query because of the rest->graphql change) <br>
  * AbilityDetails Screen - **TODO** <br>
  * MoveDetails Screen - **TODO** <br>
* Add test libraries and start the testing (planning to use espresso and junit) **TODO** <br>
* Cleanup and improve git Readme layout **TODO** <br>

## Part 2 - Offline first and improvements <br>
* Adds error messages for communication errors **TODO**<br>
* Fixes Architeture flaws **TODO**<br>
* Add Room database **TODO**<br>
* Add and configure apolloCache  **TODO**<br>
* if possible planning to add an option to download the data locally, with option to choose to download or not the images **TODO**<br>
* Check for accessibility, text-to-speak, manual tests with content readers, test colors for better contrast etc...

## Part 3 - Mount your own team <br>
* List with pokemons to choose <br>
* Choose skills and moves <br>
* Check type coverage <br>
* Check move type coverage <br>

## Bonus - Better version of mount your team<br>
* Option to filter for competitive tiers (Uber, OU, BL etc)  (Needs an api with up to date data, maybe Smogon or PokemonShowdown) <br>
* Add the filter above for pokemons, skills, moves and itens <br>
* 100% test coverage <br>


## major changes:
* Removed retrofit and addded Apollo
* Started using PokeApi Graphql
* Added localHost build variant

Credits - TODO( add all libraries and people that helped)
