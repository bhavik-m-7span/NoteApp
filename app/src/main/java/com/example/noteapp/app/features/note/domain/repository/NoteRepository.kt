import com.example.noteapp.app.core.domain.model.Note

interface NoteRepository {
    suspend fun insert(note: Note)

    suspend fun getNote(id: Int): Note?

    suspend fun update(note: Note)
}