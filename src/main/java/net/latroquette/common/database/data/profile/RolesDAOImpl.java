/**
 * 
 */
package net.latroquette.common.database.data.profile;

import net.latroquette.common.database.data.Repositories;

import org.springframework.stereotype.Repository;

import com.adi3000.common.database.hibernate.session.AbstractDAO;

/**
 *
 */
@Repository(Repositories.ROLES_REPOSITORY)
public class RolesDAOImpl extends AbstractDAO<Role> implements RolesDAO {

}
