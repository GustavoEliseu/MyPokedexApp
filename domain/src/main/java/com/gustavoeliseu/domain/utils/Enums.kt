package com.gustavoeliseu.domain.utils

import androidx.compose.ui.graphics.Color
import com.gustavoeliseu.myapplication.R


interface DefaultEnum {
    val nameId: Int
}

interface IValueEnum : DefaultEnum {
    val value: Int
}

interface IRefValueEnum : IValueEnum {
    val tintColor: Color
}

interface IIconRefValueEnum : IValueEnum {
    val drawableId: Int
}

//not exactly needed, but provides a better readability for the code
enum class ClickType(override val value: Int) : IValueEnum {
    POKEMON(0) {
        override val nameId = R.string.pokemon
    },
    ABILITY(1) {
        override val nameId = R.string.ability
    },
    MOVE(2) {
        override val nameId = R.string.move
    },
    EGG_TYPE(3) {
        override val nameId = R.string.egg_type
    },
    TYPE(4) {
        override val nameId = R.string.type
    }
}

enum class TypeEnum(override val value: Int) : IIconRefValueEnum, IRefValueEnum {
    INVALID(-1) {
        override val nameId = R.string.invalid_evolution_type
        override val tintColor = Color(103, 103, 101)
        override val drawableId = 0
    },
    NORMAL(1) {
        override val nameId = R.string.normal
        override val tintColor = Color(161, 162, 158, 255)
        override val drawableId = R.drawable.normal_icon
    },
    FIGHTING(2) {
        override val nameId = R.string.fighting
        override val tintColor = Color(210, 66, 94, 255)
        override val drawableId = R.drawable.fighting_icon
    },
    FLYING(3) {
        override val nameId = R.string.flying
        override val tintColor = Color(161, 187, 236, 255)
        override val drawableId = R.drawable.flying_icon
    },
    POISON(4) {
        override val nameId = R.string.poison
        override val tintColor = Color(183, 99, 207, 255)
        override val drawableId = R.drawable.poison_icon
    },
    GROUND(5) {
        override val nameId = R.string.ground
        override val tintColor = Color(218, 125, 76, 255)
        override val drawableId = R.drawable.ground_icon
    },
    ROCK(6) {
        override val nameId = R.string.rock
        override val tintColor = Color(201, 188, 138, 255)
        override val drawableId = R.drawable.rock_icon
    },
    BUG(7) {
        override val nameId = R.string.bug
        override val tintColor = Color(147, 188, 45, 255)
        override val drawableId = R.drawable.bug_icon
    },
    GHOST(8) {
        override val nameId = R.string.ghost
        override val tintColor = Color(94, 108, 188, 255)
        override val drawableId = R.drawable.ghost_icon
    },
    STEEL(9) {
        override val nameId = R.string.steel
        override val tintColor = Color(87, 149, 163, 255)
        override val drawableId = R.drawable.steel_icon
    },
    FIRE(10) {
        override val nameId = R.string.fire
        override val tintColor = Color(251, 167, 77, 255)
        override val drawableId = R.drawable.fire_icon
    },
    WATER(11) {
        override val nameId = R.string.water
        override val tintColor = Color(83, 156, 222, 255)
        override val drawableId = R.drawable.water_icon
    },
    GRASS(12) {
        override val nameId = R.string.grass
        override val tintColor = Color(96, 189, 88, 255)
        override val drawableId = R.drawable.grass_icon
    },
    ELECTRIC(13) {
        override val nameId = R.string.electric
        override val tintColor = Color(242, 216, 79, 255)
        override val drawableId = R.drawable.electric_icon
    },
    PSYCHIC(14) {
        override val nameId = R.string.psychic
        override val tintColor = Color(250, 133, 130, 255)
        override val drawableId = R.drawable.psychic_icon
    },
    ICE(15) {
        override val nameId = R.string.ice
        override val tintColor = Color(118, 208, 192, 255)
        override val drawableId = R.drawable.ice_icon
    },
    DRAGON(16) {
        override val nameId = R.string.dragon
        override val tintColor = Color(13, 107, 200, 255)
        override val drawableId = R.drawable.dragon_icon
    },
    DARK(17) {
        override val nameId = R.string.dark
        override val tintColor = Color(88, 86, 97, 255)
        override val drawableId = R.drawable.dark_icon
    },
    FAIRY(18) {
        override val nameId = R.string.fairy
        override val tintColor = Color(238, 145, 230, 255)
        override val drawableId = R.drawable.fairy_icon
    },
    UNKNOWN(10001) {
        override val nameId = R.string.unknown
        override val tintColor = Color(161, 162, 158, 255)
        override val drawableId = 0
    },
    SHADOW(10002) {
        override val nameId = R.string.shadow
        override val tintColor = Color.Black
        override val drawableId = 0
    };

    companion object {
        private val map = TypeEnum.values().associateBy(TypeEnum::value)
        fun fromInt(value: Int?): TypeEnum {
            return if (value != null) map[value] ?: INVALID else INVALID
        }

        fun TypeEnum.isValid(): Boolean {
            return this != INVALID && this != UNKNOWN && this != SHADOW
        }
    }
}

enum class ColorEnum(override val value: Int) : IRefValueEnum {
    INVALID(-1) {
        override val nameId = 0
        override val tintColor = Color(103, 103, 101)
    },
    BLACK(1) {
        override val tintColor = Color.Black
        override val nameId = R.string.black
    },
    BLUE(2) {
        override val tintColor = Color(53, 91, 245)
        override val nameId = R.string.blue
    },
    BROWN(3) {
        override val tintColor = Color(135, 99, 58)
        override val nameId = R.string.brown
    },
    GRAY(4) {
        override val tintColor = Color(197, 198, 199)
        override val nameId = R.string.app_name
    },
    GREEN(5) {
        override val tintColor = Color(142, 211, 153)
        override val nameId = R.string.green
    },
    PINK(6) {
        override val tintColor = Color(251, 202, 224)
        override val nameId = R.string.pink
    },
    PURPLE(7) {
        override val tintColor = Color(90, 36, 123)
        override val nameId = R.string.purple
    },
    RED(8) {
        override val tintColor = Color(189, 39, 52)
        override val nameId = R.string.red
    },
    WHITE(9) {
        override val tintColor = Color.White
        override val nameId = R.string.white
    },
    YELLOW(10) {
        override val tintColor = Color(229, 183, 35)
        override val nameId = R.string.yellow
    };

    companion object {
        private val map = values().associateBy(ColorEnum::value)
        fun fromInt(value: Int?): ColorEnum {
            return if (value != null) map[value] ?: INVALID else INVALID
        }
    }
}

enum class EvolutionRequirementTypeEnum(override val value: Int) : IIconRefValueEnum {
    INVALID(-1) {
        override val nameId = R.string.invalid_evolution_type
        override val drawableId = 0
    },
    FRIENDSHIP(-2){
        override val nameId = R.string.friendship
        override val drawableId = R.drawable.love
    },
    BEAUTY(-3){
        override val nameId = R.string.beauty
        override val drawableId = R.drawable.beauty
    },
    DAY(-4){
        override val nameId = R.string.day
        override val drawableId = R.drawable.day
    },
    NIGHT(-5){
        override val nameId = R.string.night
        override val drawableId = R.drawable.night
    },
    SUN_STONE(80) {
        override val nameId = R.string.sun_stone
        override val drawableId = R.drawable.sun_stone
    },
    MOON_STONE(81) {
        override val nameId = R.string.moon_stone
        override val drawableId = R.drawable.moon_stone
    },
    FIRE_STONE(82) {
        override val nameId = R.string.fire_stone
        override val drawableId = R.drawable.fire_stone
    },
    THUNDER_STONE(83) {
        override val nameId = R.string.thunder_stone
        override val drawableId = R.drawable.thunder_stone
    },
    WATER_STONE(84) {
        override val nameId = R.string.water_stone
        override val drawableId = R.drawable.water_stone
    },
    LEAF_STONE(85) {
        override val nameId = R.string.leaf_stone
        override val drawableId = R.drawable.leaf_stone
    },
    SHINY_STONE(107) {
        override val nameId = R.string.shiny_stone
        override val drawableId = R.drawable.shiny_stone
    },
    DUSK_STONE(108) {
        override val nameId = R.string.dusk_stone
        override val drawableId = R.drawable.dusk_stone
    },
    DAWN_STONE(109) {
        override val nameId = R.string.dawn_stone
        override val drawableId = R.drawable.dawn_stone
    },
    OVAL_STONE(110) {
        override val nameId = R.string.oval_stone
        override val drawableId = R.drawable.oval_stone
    },
    DRAGON_SCALE(212) {
        override val nameId = R.string.dragon_scale
        override val drawableId = R.drawable.dragon_scale
    },
    UP_GRADE(229) {
        override val nameId = R.string.up_grade
        override val drawableId = R.drawable.up_grade
    },
    PROTECTOR(298) {
        override val nameId = R.string.protector
        override val drawableId = R.drawable.protector
    },
    ELECTIRIZER(299) {
        override val nameId = R.string.electirizer
        override val drawableId = R.drawable.electirizer
    },
    MAGMARIZER(300) {
        override val nameId = R.string.magmarizer
        override val drawableId = R.drawable.magmarizer
    },
    DUBIOUS_DISC(301) {
        override val nameId = R.string.dubious_disc
        override val drawableId = R.drawable.dubious_disc
    },
    REAPER_CLOTH(302) {
        override val nameId = R.string.reaper_cloth
        override val drawableId = R.drawable.reaper_cloth
    },
    PRISM_SCALE(580) {
        override val nameId = R.string.prism_scale
        override val drawableId = R.drawable.prism_scale
    },
    WHIPPED_DREAM(686) {
        override val nameId = R.string.whipped_dream
        override val drawableId = R.drawable.whipped_dream
    },
    SACHET(687) {
        override val nameId = R.string.sachet
        override val drawableId = R.drawable.sachet
    },
    ICE_STONE(885) {
        override val nameId = R.string.ice_stone
        override val drawableId = R.drawable.ice_stone
    },
    STRAWBERRY_SWEET(1167) {
        override val nameId = R.string.strawberry_sweet
        override val drawableId = R.drawable.strawberry_sweet
    },
    LOVE_SWEET(1168) {
        override val nameId = R.string.love_sweet
        override val drawableId = R.drawable.love_sweet
    },
    BERRY_SWEET(1169) {
        override val nameId = R.string.berry_sweet
        override val drawableId = R.drawable.berry_sweet
    },
    CLOVER_SWEET(1170) {
        override val nameId = R.string.clover_sweet
        override val drawableId = R.drawable.clover_sweet
    },
    FLOWER_SWEET(1171) {
        override val nameId = R.string.flower_sweet
        override val drawableId = R.drawable.flower_sweet
    },
    STAR_SWEET(1172) {
        override val nameId = R.string.star_sweet
        override val drawableId = R.drawable.star_sweet
    },
    RIBBON_SWEET(1173) {
        override val nameId = R.string.ribbon_sweet
        override val drawableId = R.drawable.ribbon_sweet
    },
    SWEET_APPLE(1174) {
        override val nameId = R.string.sweet_apple
        override val drawableId = R.drawable.sweet_apple
    },
    TART_APPLE(1175) {
        override val nameId = R.string.tart_apple
        override val drawableId = R.drawable.tart_apple
    },
    CRACKED_POT(1311) {
        override val nameId = R.string.cracked_pot
        override val drawableId = R.drawable.cracked_pot
    },
    CHIPPED_POT(1312) {
        override val nameId = R.string.chipped_pot
        override val drawableId = R.drawable.chipped_pot
    },
    MALICIOUS_ARMOR(1677) {
        override val nameId = R.string.malicious_armor
        override val drawableId = R.drawable.malicious_armor
    },
    SYRUPY_APPLE(2109) {
        override val nameId = R.string.syrupy_apple
        override val drawableId = R.drawable.syrupy_apple
    },
    UNREMARKABLE_TEACUP(2110) {
        override val nameId = R.string.unremarkable_teacup
        override val drawableId = R.drawable.unremarkable_teacup
    },
    MASTERPIECE_TEACUP(2111) {
        override val nameId = R.string.masterpiece_teacup
        override val drawableId = R.drawable.masterpiece_teacup
    },
    GALARICA_CUFF(1633) {
        override val nameId = R.string.galarica_cuff
        override val drawableId = R.drawable.galarica_cuff
    },
    GALARICA_WREATH(1643) {
        override val nameId = R.string.galarica_wreath
        override val drawableId = R.drawable.galarica_wreath
    },
    BLACK_AUGURITE(10001) {
        override val nameId = R.string.black_augurite
        override val drawableId = R.drawable.black_augurite
    },
    PEAT_BLOCK(10002) {
        override val nameId = R.string.peat_block
        override val drawableId = R.drawable.peat_block
    },
    KINGS_ROCK(198) {
        override val nameId = R.string.kings_rock
        override val drawableId = R.drawable.kings_rock
    },
    DEEP_SEA_TOOTH(203) {
        override val nameId = R.string.deep_sea_tooth
        override val drawableId = R.drawable.deep_sea_tooth
    },
    DEEP_SEA_SCALE(204) {
        override val nameId = R.string.deep_sea_scale
        override val drawableId = R.drawable.deep_sea_scale
    },
    METAL_COAT(210) {
        override val nameId = R.string.metal_coat
        override val drawableId = R.drawable.metal_coat
    },
    UPGRADE(229) {
        override val nameId = R.string.upgrade
        override val drawableId = R.drawable.up_grade
    },
    RAZOR_CLAW(303) {
        override val nameId = R.string.razor_claw
        override val drawableId = R.drawable.razor_claw
    },
    RAZOR_FANG(304) {
        override val nameId = R.string.razor_fang
        override val drawableId = R.drawable.razor_fang
    };


    companion object {
        private val map = values().associateBy(EvolutionRequirementTypeEnum::value)

        fun fromInt(value: Int?): EvolutionRequirementTypeEnum {
            return if (value != null) map[value] ?: INVALID else INVALID
        }

        fun getDayOrNight(value: String?): EvolutionRequirementTypeEnum {
            return when(value?.lowercase()){
                "day"->{DAY}
                "night"->{NIGHT}
                else->{INVALID}
            }
        }

    }
}

enum class GenderEnum(override val value: Int) : IIconRefValueEnum {
    INVALID(-1) {
        override val nameId = R.string.invalid
        override val drawableId = 0
    },
    FEMALE(1) {
        override val nameId = R.string.female
        override val drawableId = R.drawable.female
    },
    MALE(2) {
        override val nameId = R.string.male
        override val drawableId = R.drawable.male
    };
    companion object {
        private val map = GenderEnum.values().associateBy(GenderEnum::value)
        fun fromInt(value: Int?): GenderEnum {
            return if (value != null) map[value] ?: INVALID else INVALID
        }
    }
}

enum class EvolutionTypeEnum(override val value: Int) : IValueEnum {
    INVALID(-1) {
        override val nameId = R.string.invalid_evolution_type
    },
    LEVEL(1) {
        override val nameId = R.string.level_up
    },
    TRADE(2) {
        override val nameId = R.string.trade
    },
    USEITEM(3) {
        override val nameId = R.string.use_item
    },
    SHED(4) {
        override val nameId = R.string.shed
    },
    SPIN(5) {
        override val nameId = R.string.spin
    },
    TOWERDARKNESS(6) {
        override val nameId = R.string.tower_of_darkness
    },
    TOWERWATERS(7) {
        override val nameId = R.string.tower_of_waters
    },
    CRITICALHITS(8) {
        override val nameId = R.string.three_critical_hits
    },
    DAMAGE(9) {
        override val nameId = R.string.take_damage
    },
    OTHER(10) {
        override val nameId = R.string.other
    },
    AGILEMOVE(11) {
        override val nameId = R.string.agile_style_move
    },
    STRONGMOVE(12) {
        override val nameId = R.string.strong_style_move
    },
    RECOIL(13) {
        override val nameId = R.string.recoil_damage
    };

    companion object {
        private val map = EvolutionTypeEnum.values().associateBy(EvolutionTypeEnum::value)
        fun fromInt(value: Int?): EvolutionTypeEnum {
            return if (value != null) EvolutionTypeEnum.map[value] ?: INVALID else INVALID
        }
    }
}