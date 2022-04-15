package project.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Author getRandomAuthor() {
       long size = this.authorRepository.count();

       int authorId= new Random().nextInt((int) size) +1;

       return this.authorRepository.findById(authorId).get();
    }
}
