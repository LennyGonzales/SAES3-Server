package fr.univ_amu.iut.database.dao;

public interface DAO <Entity,Key> {
    /**
     * Allows removal of a tuple from the base
     *
     * @param obj Objet à supprimer dans la base
     */
    boolean delete(Entity obj);

    /**
     * Allows to create a tuple in the database with an object
     *
     * @param obj Objet à insérer dans la base
     */
    Entity insert(Entity obj);

    /**
     * Allows to update a tuple in the database with an object
     *
     * @param obj Objet à mettre à jour dans la base
     */
    boolean update(Entity obj);
}