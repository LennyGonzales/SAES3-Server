package fr.univ_amu.iut.database.dao;

import java.sql.SQLException;

/**
 * The signature of the basic methods of a JDBC
 * @param <Entity>
 * @param <Key>
 * @author LennyGonzales
 */
public interface DAO <Entity,Key> {
    /**
     * Allows removal of a tuple from the base
     * @param obj Object to delete from the database
     * @return true - The deletion went well | false - The deletion didn't go well
     * @throws SQLException if the deletion didn't go well
     */
    boolean delete(Entity obj) throws SQLException;

    /**
     * Allows to create a tuple in the database with an object
     * @param obj Object to insert into the database
     * @return the tuple inserted
     * @throws SQLException if the insertion didn't go well
     */
    Entity insert(Entity obj) throws SQLException;

    /**
     * Allows to update a tuple in the database with an object
     * @param obj Object to update in the database
     * @return true - The update went well | false - The update didn't go well
     */
    boolean update(Entity obj);
}