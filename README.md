This is a petProject that utilizes Apollo with the GraphQL pokeApi to fill a pokedex. I'm dividing this project in 3 parts:

<p align="center">
  <img src="https://github.com/GustavoEliseu/MyPokedexApp/assets/30469845/47b093bf-3540-46af-8b8f-3d0af269cefd" />
</p>

Part 1 - Pokedex - Testing features that i never used so expect a messy code on this part
-Implement the basic pokedex feature:
  *Search - TODO (partially done, needs layout and small fixes)
  *List - Done (need small layout fixes)
  *Use Android Palette to get the pokemon color - Done
  *PokemonDetails Screen - TODO (Redo the query because of the rest->graphql change)
  *AbilityDetails Screen - TODO
  *MoveDetails Screen - TODO
-Add test libraries and start the testing (planning to use espresso and junit)
-Cleanup and improve git Readme layout

Part 2 - Offline first option 
-Adds error messages for communication errors
-Fixes Architeture flaws
-Adds Room database
-Adds apolloCache and configures it
-if possible planning to add an option to download the data locally, with option to choose to download or not the images

Part 3 - Mount your own team
-List with pokemons to choose
-Choose skills and moves
-Check type coverage
-Check move type coverage

Bonus - Better version of mount your team
-Option to filter for competitive tiers (Uber, OU, BL etc)  (Needs an api up to date data, maybe Smogon or PokemonShowdown)
-Add the filter above for pokemons, skills, moves and itens
-100% test coverage



major changes:
-Removed retrofit and addded Apollo
-Started using PokeApi Graphql
-Added localHost build variant



Credits - TODO( add all libraries and people that helped)



