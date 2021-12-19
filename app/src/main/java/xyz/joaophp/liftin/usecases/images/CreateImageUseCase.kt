package xyz.joaophp.liftin.usecases.images

import android.net.Uri
import xyz.joaophp.liftin.data.models.User
import xyz.joaophp.liftin.utils.Either
import xyz.joaophp.liftin.utils.failures.Failure

interface CreateImageUseCase {

    suspend operator fun invoke(user: User, fileUri: Uri): Either<Failure, Uri>
}