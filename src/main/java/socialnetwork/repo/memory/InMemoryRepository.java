package socialnetwork.repo.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repo.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private Validator<E> validator;
    private Map<ID, E> entities;

    public InMemoryRepository(Validator<E> validator){
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("Id must not be null!\n");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("Entity must not be null!\n");

        validator.validate(entity);

        if(entities.get(entity.getId()) != null) {
            throw new IllegalArgumentException("An entity with that ID already exists!\n");
        }
        else
            entities.put(entity.getId(),entity);
        return entity;
    }

    @Override
    public E delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException("Id must not be null!\n");

        if(entities.get(id) != null) {
            E entity = entities.get(id);
            if(entity != null) {
                entities.remove(id);
                return entity;
            }
        }
        throw new IllegalArgumentException("Entity does not exist!\n");

    }

    @Override
    public E update(E entity) {
        //acelasi id alta entitate

        if(entity == null)
            throw new IllegalArgumentException("Entity must not be null!\n");

        validator.validate(entity);

        if(entities.get(entity.getId()) == null) {
            throw new IllegalArgumentException("Entity must not be null!\n");
        }
        entities.put(entity.getId(),entity);
        return entity;

    }
}