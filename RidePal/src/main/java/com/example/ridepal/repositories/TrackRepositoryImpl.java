package com.example.ridepal.repositories;

import com.example.ridepal.exceptions.EntityNotFoundException;
import com.example.ridepal.models.Genre;
import com.example.ridepal.models.Track;
import com.example.ridepal.repositories.contracts.TrackRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class TrackRepositoryImpl extends AbstractCrudRepository<Track> implements TrackRepository {
    @Autowired
    public TrackRepositoryImpl(SessionFactory sessionFactory, Class<Track> clazz) {
        super(sessionFactory, clazz);
    }

    @Override
    public Set<Track> generateRandomTrackByGenre(Genre genre, int tracksNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<Track> query = session.createQuery("FROM Track WHERE genre=:genreId ORDER BY rand()");
            query.setParameter("genreId", genre.getGenreId());
            query.setParameter("tracksNumber", tracksNumber);
            query.setMaxResults(tracksNumber);
            List<Track> result = query.list();
            Set<Track> resultSet = new HashSet<>(result);
            if (result.isEmpty()) {
                throw new EntityNotFoundException("Tracks", "name", genre.getGenreName());
            }
            return resultSet;
        }
    }
}
