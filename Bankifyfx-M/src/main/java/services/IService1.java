package services;

import java.sql.SQLException;
import java.util.List;

public interface IService1<T> {

    void add(T t) throws SQLException;

    void edit(T t) throws SQLException;

    void delete(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    T getById(int id) throws  SQLException;
}