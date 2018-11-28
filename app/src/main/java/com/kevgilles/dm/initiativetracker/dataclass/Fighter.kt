package com.kevgilles.dm.initiativetracker.dataclass

import android.os.Parcel
import android.os.Parcelable
import androidx.core.app.ActivityCompat

open class Fighter() : ActivityCompat(), Parcelable{
    open var name: String = ""
    open var armorClass: Int = 0
    open var hitPoints: Int = 0
    open var initiativeModifier: Int = 0
    open var rolledInitiative: Int = 0
    open var currentHp: Int = 0

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        armorClass = parcel.readInt()
        hitPoints = parcel.readInt()
        initiativeModifier = parcel.readInt()
        rolledInitiative = parcel.readInt()
        currentHp = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(armorClass)
        parcel.writeInt(hitPoints)
        parcel.writeInt(initiativeModifier)
        parcel.writeInt(rolledInitiative)
        parcel.writeInt(currentHp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Fighter> {
        override fun createFromParcel(parcel: Parcel): Fighter {
            return Fighter(parcel)
        }

        override fun newArray(size: Int): Array<Fighter?> {
            return arrayOfNulls(size)
        }
    }
}