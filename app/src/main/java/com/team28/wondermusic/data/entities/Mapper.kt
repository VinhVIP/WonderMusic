package com.team28.wondermusic.data.entities

import com.team28.wondermusic.data.database.question.QuestionEntity
import com.team28.wondermusic.data.models.LineLyric
import com.team28.wondermusic.data.models.Song


fun Question.toQuestionEntity(): QuestionEntity {
    return QuestionEntity(
        questionId = this.questionId,
        title = this.title
    )
}


// Chuyển 1 dòng String lyric thành đối tượng object LineLyric
fun String.convertToLineLyric(): LineLyric {
    val closeBracketIndex = indexOf(']')
    if (closeBracketIndex != -1) {
        val time = substring(1, closeBracketIndex)

        val twoDot = time.indexOf(':')
        val dot = time.indexOf('.')

        val minute = time.substring(1, twoDot).toInt()
        val second = time.substring(twoDot + 1, dot).toInt()
        val millis = time.substring(dot + 1).toInt()

        val timeMillis = minute * 60 * 1000 + second * 1000 + millis * 10

        return LineLyric(timeMillis, substring(closeBracketIndex + 1).trim())
    }

    return LineLyric(0, this)
}

fun Song.singersToString(): String {
    var names = ""
    singers?.let {
        for (singer in it) {
            names += singer.accountName + ", "
        }
        if (names.length > 1) names = names.substring(0, names.length - 2)
        return names
    }
    return ""
}

fun Int.toTimeFormat(): String {
    val hour = this / 3600
    val minute = (this % 3600) / 60
    val second = (this % 3600) % 60

    return if (hour == 0) {
        if (minute < 10) String.format("%d:%02d", minute, second)
        else String.format("%02d:%02d", minute, second)
    } else {
        String.format("%d:%02d:%02d", hour, minute, second)
    }
}


fun QuestionEntity.toQuestion(): Question {
    return Question(
        questionId = this.questionId,
        title = this.title
    )
}


fun List<QuestionEntity>.toListQuestions(): List<Question> {
    return this.map { it.toQuestion() }
}

fun List<Question>.toListQuestionEntities(): List<QuestionEntity> {
    return this.map { it.toQuestionEntity() }
}