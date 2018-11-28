package com.kevgilles.dm.initiativetracker.database

import androidx.room.*
import com.kevgilles.dm.initiativetracker.dataclass.*

@Dao
interface CampaignDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCampaign(campaign: Campaign): Long

    @Query("SELECT * FROM campaign")
    fun getAllCampaigns(): MutableList<Campaign>
}

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTeam(team: Team): Long

    @Update
    fun updateTeamName(team: Team): Int

    @Query("SELECT * FROM team WHERE campaign_id_fk =:campaignId")
    fun getTeamFromCampaignId(campaignId: Long): Team
}

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: Character): Long

    @Update
    fun updateCharacter(character: Character): Int

    @Query("DELETE FROM character WHERE character_id = :id")
    fun deleteCharacter(id: Long)

    @Query("SELECT * FROM character WHERE team_id_fk = :teamId")
    fun getAllCharacterFromTeam(teamId: Long): MutableList<Character>
}

@Dao
interface EncounterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEncounter(encounter: Encounter): Long

    @Update
    fun updateEncounter(encounter: Encounter): Int

    @Query("DELETE FROM encounter WHERE encounter_id = :id")
    fun deleteEncounter(id: Long)

    @Query("SELECT * FROM encounter WHERE campaign_id_fk = :campaignId")
    fun getAllEncountersFromCampaign(campaignId: Long): MutableList<Encounter>
}

@Dao
interface CreatureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCreature(creature: Creature): Long

    @Update
    fun updateCreature(creature: Creature): Int

    @Query("DELETE FROM creature WHERE creature_id = :id")
    fun deleteCreature(id: Long)

    @Query("SELECT * FROM creature INNER JOIN encountercreature ON encountercreature.creature_id_fk = creature.creature_id WHERE encountercreature.encounter_id_fk = :encounterId")
    fun getAllCreaturesFromEncounterId(encounterId: Long): MutableList<Creature>


//    @Query("SELECT creature_id, creature_name, creature_ac, creature_hp, creature_init FROM creature INNER JOIN encountercreature ON encountercreature.creature_id_fk = creature.creature_id WHERE encountercreature.encounter_id_fk = :encounterId")
//    fun getAllCreaturesFromEncounterId(encounterId: Long): MutableList<Creature>

}

@Dao
interface EncounterCreatureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEncounterCreature(encounterCreature: EncounterCreature)
}
