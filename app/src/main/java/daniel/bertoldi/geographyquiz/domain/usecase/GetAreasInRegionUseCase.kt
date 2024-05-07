package daniel.bertoldi.geographyquiz.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetAreasInRegionUseCase {

    suspend operator fun invoke(region: String): Flow<List<String>>
}