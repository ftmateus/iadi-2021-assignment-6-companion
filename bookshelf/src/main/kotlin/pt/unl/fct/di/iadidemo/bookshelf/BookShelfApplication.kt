package pt.unl.fct.di.iadidemo.bookshelf

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pt.unl.fct.di.iadidemo.bookshelf.domain.*
import javax.transaction.Transactional

@SpringBootApplication
class SecurityApplication(
    val books:BookRepository,
    val users:UserRepository,
    val roles:RoleRepository,
    val authorsRep:AuthorRepository
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {

        val r1 = RoleDAO("ADMIN")
        val r2 = RoleDAO("REVIEWER")
        val r3 = RoleDAO("USER")
        roles.saveAll(listOf(r1, r2, r3))

        // Dummy users
        users.save(UserDAO("user",BCryptPasswordEncoder().encode("password"), listOf(r3),"User"))
        users.save(UserDAO("admin",BCryptPasswordEncoder().encode("password"), listOf(r1),"Admin"))

        val u1 = UserDAO("user1",BCryptPasswordEncoder().encode("password1"),listOf(r3,r2),"User 1")
        users.save(u1)

        val u2 = UserDAO("admin1",BCryptPasswordEncoder().encode("password1"), listOf(r1),"Admin 1")
        users.save(u2)

        val authorsList : List<String> = listOf("Philip K. Dick",
            "George R.R. Martin",
            "Jane Austen",
            "Mark Twain",
            "J.K Rowling",
            "George Orwel",
            "Maya Angelou",
            "Henry Miller"
        );

        val authorsDAOs : List<AuthorDAO> = authorsList.map { author ->
            val aDAO : AuthorDAO = AuthorDAO(0, author)
            authorsRep.save(aDAO)
            aDAO
        }

        val a1 = authorsDAOs.get(0)

        val b1 = BookDAO(0,"Ubik", mutableListOf(a1), listOf(ImageDAO(0, "https://covers.openlibrary.org/b/id/9251896-L.jpg")))
        val b2 = BookDAO(0,"Do Androids Dream of Electric Sheep?", mutableListOf(a1), listOf(ImageDAO(0, "https://covers.openlibrary.org/b/id/11153217-L.jpg")))
        val b3 = BookDAO(0,"The Man in the High Castle", mutableListOf(a1), listOf(ImageDAO(0, "https://covers.openlibrary.org/b/id/10045188-L.jpg")))

        books.saveAll(listOf(b1,b2,b3))
    }

}

fun main(args: Array<String>) {
    runApplication<SecurityApplication>(*args)
}
