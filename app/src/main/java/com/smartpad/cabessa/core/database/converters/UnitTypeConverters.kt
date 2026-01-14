package com.smartpad.cabessa.core.database.converters

import androidx.room.TypeConverter
import com.smartpad.cabessa.modules.drugs.common.enums.EnumUnitTypes

class UnitTypeConverters {
    /**
     * De Enum a String para guardar en la DB
     */
    @TypeConverter
    fun fromUnitType(unitType: EnumUnitTypes): String {
        return unitType.name
    }

    /**
     * De String a Enum para leer de la DB
     */
    @TypeConverter
    fun toUnitType(value: String): EnumUnitTypes {
        return enumValueOf<EnumUnitTypes>(value)
    }
}