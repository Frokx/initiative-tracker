package com.kevgilles.dm.initiativetracker.dataclass

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(foreignKeys = [
    ForeignKey(entity = Team::class,
        parentColumns = [Team.TEAM_ID],
        childColumns = [Character.TEAM_ID_FK])],
    indices = [
        Index(Character.TEAM_ID_FK)
    ])
data class Character(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CHARACTER_ID)
    var id: Long = 0,

    @ColumnInfo(name = CHARACTER_NAME)
    override var name: String,

    @ColumnInfo(name = CHARACTER_AC)
    override var armorClass: Int,

    @ColumnInfo(name = CHARACTER_HP)
    override var hitPoints: Int,

    @ColumnInfo(name = CHARACTER_INIT)
    override var initiativeModifier: Int,

    @Ignore
    override var rolledInitiative: Int = 0,

    // Foreign Key
    @ColumnInfo(name = TEAM_ID_FK)
    var teamId: Long = 0
) : Parcelable, Fighter(){

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong()
    )
    constructor(character: Character): this(
        name = character.name,
        armorClass = character.armorClass,
        hitPoints = character.hitPoints,
        initiativeModifier = character.initiativeModifier
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(armorClass)
        parcel.writeInt(hitPoints)
        parcel.writeInt(initiativeModifier)
        parcel.writeInt(rolledInitiative)
        parcel.writeLong(teamId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Character> {
        const val CHARACTER_ID: String = "character_id"
        const val CHARACTER_NAME: String = "character_name"
        const val CHARACTER_AC: String = "character_ac"
        const val CHARACTER_HP: String = "character_hp"
        const val CHARACTER_INIT: String = "character_init"
        const val TEAM_ID_FK: String = "team_id_fk"

        override fun createFromParcel(parcel: Parcel): Character {
            return Character(parcel)
        }

        override fun newArray(size: Int): Array<Character?> {
            return arrayOfNulls(size)
        }
    }
}