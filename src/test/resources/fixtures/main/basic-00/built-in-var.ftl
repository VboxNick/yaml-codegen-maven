-- Accessing model
message = ${model.content.message}

-- Built-in variables
modelFileName = ${model.file.name}
outputFileName = ${output.file.name}
outputFilePath = ${output.file.path}

-- Test outputFilePathTraversal
outputFileParentName = ${output.file.parentFile.name}