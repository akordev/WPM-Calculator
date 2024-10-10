package com.example.typinganalyze.typing.usecase

class GetTextToTypeUseCase {

    // In real case probably we get it from remote source like firebase config or our backend service
    operator fun invoke() =
        "He thought he would light the fire when he got inside," +
                " and make himself some breakfast, just to pass away the time;" +
                " but he did not seem able to handle anything from a scuttleful of coals to a"

}