package socialnetwork.repo.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repo.memory.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;



public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();

    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This functions stores all the entities' data into file
     */
    protected void writeToFile(){
        try (BufferedWriter buff = new BufferedWriter(new FileWriter(fileName))) {
            super.findAll().forEach(entity ->{
                try {
                    buff.write(createEntityAsString(entity));
                    buff.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function saves an entity to file without rewriting all of them
     * @param entity - E
     */
    protected void appendToFile(E entity){
        try (BufferedWriter buff = new BufferedWriter(new FileWriter(fileName, true))){
            try {
                buff.write(createEntityAsString(entity));
                buff.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public abstract E extractEntity(List<String> attributes);

    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e!=null)
        {
            appendToFile(entity);
            return null;
        }
        return e;

    }

    @Override
    public E update(E entity) {
        if(super.update(entity) != null){
            writeToFile();
            return null;
        }
        return entity;
    }

    @Override
    public E delete(ID id) {
        E entity = super.delete(id);
        if(entity != null) {
            writeToFile();
            return entity;
        }
        return null;
    }




}

