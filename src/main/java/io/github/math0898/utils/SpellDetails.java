package io.github.math0898.utils;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * SpellDetails contain the details for spells.
 *
 * @param id The id of the spell.
 * @param spellName The name of this spell.
 * @param description The description of this spell.
 * @param lastGrabbed How long ago these spell details were grabbed.
 *
 * @author Sugaku
 */
@JsonSerialize
public record SpellDetails (long id, String spellName, String description, long lastGrabbed) { }
