package com.kevgilles.dm.initiativetracker.dataclass

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Creature(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CREATURE_ID)
    var id: Long = 0,

    @ColumnInfo(name = CREATURE_NAME)
    override var name: String,

    @ColumnInfo(name = CREATURE_AC)
    override var armorClass: Int,

    @ColumnInfo(name = CREATURE_HP)
    override var hitPoints: Int,

    @ColumnInfo(name = CREATURE_INIT)
    override var initiativeModifier: Int,

    @Ignore
    override var rolledInitiative: Int = 0
): Fighter(), Parcelable {

    companion object CREATOR : Parcelable.Creator<Creature> {
        const val CREATURE_ID: String = "creature_id"
        const val CREATURE_NAME: String = "creature_name"
        const val CREATURE_AC: String = "creature_ac"
        const val CREATURE_HP: String = "creature_hp"
        const val CREATURE_INIT: String = "creature_init"

        override fun createFromParcel(parcel: Parcel): Creature {
            return Creature(parcel)
        }

        override fun newArray(size: Int): Array<Creature?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    constructor(creature: Creature): this(
        name = creature.name,
        armorClass = creature.armorClass,
        hitPoints = creature.hitPoints,
        initiativeModifier = creature.initiativeModifier
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(armorClass)
        parcel.writeInt(hitPoints)
        parcel.writeInt(initiativeModifier)
        parcel.writeInt(rolledInitiative)
    }

    override fun describeContents(): Int {
        return 0
    }
}