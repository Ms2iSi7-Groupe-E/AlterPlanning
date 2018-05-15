package fr.nantes.eni.alterplanning.service.dao;

import fr.nantes.eni.alterplanning.dao.mysql.entity.UserEntity;
import fr.nantes.eni.alterplanning.dao.mysql.repository.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by ughostephan on 23/06/2017.
 */
@Service
public class UserDAOService implements UserDetailsService {

    @Resource
    private IUserRepository repository;

    public List<UserEntity> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public UserEntity findById(final int id) {
        return repository.findById(id).orElse(null);
    }

    public UserEntity findByEmailAndActive(final String email) {
        return repository.findByEmailAndActive(email);
    }

    public UserEntity create(final UserEntity user) {
        return repository.save(user);
    }

    public void update(final UserEntity user) {
        UserEntity entity = repository.findById(user.getId()).orElse(null);

        if (entity == null)
            throw new EntityNotFoundException();

        repository.save(user);
    }

    public void delete(final int id) {
        UserEntity entityToDelete = repository.findById(id).orElse(null);

        if (entityToDelete == null)
            throw new EntityNotFoundException();

        repository.delete(entityToDelete);
    }

    /**
     * Email already used boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean emailAlreadyUsed(final String email) {
        return repository.countByEmail(email) > 0;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final UserEntity user = findByEmailAndActive(email);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }
}
