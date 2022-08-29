# Cryptography & Networks Security - Team Project

## AES (Advanced Encryption Standard) Implementation


<p align="center">
<img 
src="https://upload.wikimedia.org/wikipedia/commons/5/50/AES_%28Rijndael%29_Round_Function.png" 
align="center" 
height="480" 
width="320"
alt="Advanced Encryption Standard"
>
</p>

## Team Members: 
- Faris H. Abuali
- Lena Samaha
- Waleed Zriqi
- Weaam Ghannam


## Task definition:
It is an application to test AES modules (S-BOX, Shift rows, mix columns), which is the 16-byte output of "add round key" is the input to the first S-Box as 4*4 matrix called state,  to perform a byte-to-byte substitution which is the input to the next level (shift rows) which rotate each row by specific value (except for the first row). The output of this level enters the next level (Mix column) which multiplies the state matrix with fixed matrix.

## Definition of Algorithm:
First we have to talk about Advanced Encryption Standard algorithm (AES), It also Known as Rijndael algorithm, It is a symmetric block cipher algorithm that takes a block size of 128 bits and converts them into cipher text using keys of 128, 192 and 256 bits. AES contains several rounds; the number of rounds is dependent on key length.
The plain text size is 16 byte which is divided to 4 words, each word is the column of 4*4 matrix, as shown in this figure:
![image](https://user-images.githubusercontent.com/54215462/187033125-f0d8d033-1363-4f02-8d81-34e5a29bc1a5.png)
<br/>
This matrix is called state matrix, which is modified at each round.

Each round has 4 stages (Except for the last round):
1.	### Substitute bytes – Uses clever lockup table (S-Box) to map one byte to another
<p align="center">
<img 
src="https://user-images.githubusercontent.com/54215462/187033150-4270343c-abfa-4146-911b-9b77b075699a.png" 
align="center" 
height="480" 
width="480"
alt="Substitute bytes"
>
</p>

2.	### Shift rows – A simple permutation.
<p align="center">
<img 
src="https://user-images.githubusercontent.com/54215462/187033152-ae3e928d-0f98-4220-8798-2ad217e9f099.png" 
align="center" 
height="480" 
width="480"
alt="Shift rows"
>
</p>

3.	### Mix columns – A substitution that makes use of arithmetic over GF(28).
<p align="center">
<img 
src="https://user-images.githubusercontent.com/54215462/187033158-22ca32ca-ecdb-483b-a577-471389b17562.png" 
align="center" 
height="480" 
width="480"
alt="Mix columns"
>
</p>


4.	### Add round key – A simple bitwise XOR of the current block with the portion of the expanded key.
<p align="center">
<img 
src="https://user-images.githubusercontent.com/54215462/187033161-86459d8d-7c9b-4422-adb4-ffff8cfb87e0.png" 
align="center" 
height="480" 
width="480"
alt="Mix columns"
>
</p>


## The overall structure of AES:
<p align="center">
<img 
src="https://user-images.githubusercontent.com/54215462/187033170-08bbae53-51fe-46d2-9a02-0ac0c38eb885.png" 
align="center" 
height="800" 
width="500"
alt="The overall structure of AES"
>
</p>
<br/>
Notice that the last round doesn't contain mix column stage.

## One Round of AES:
<p align="center">
<img 
src="https://user-images.githubusercontent.com/54215462/187033181-a3ae7914-5b99-46fc-861f-36c2d0140f73.png" 
align="center" 
height="900" 
width="600"
alt="One Round of AES"
>
</p>

## Testing Everything (Encoding, Encryption, Decryption, then Decoding):
<img width="548" alt="image" src="https://user-images.githubusercontent.com/54215462/187188034-7b9c9e93-da95-481c-ac2b-40075fe0029b.png">
<img width="440" alt="image" src="https://user-images.githubusercontent.com/54215462/187188221-5b861229-9df7-4c32-a0ff-d3f62962f9cd.png">

<img width="581" alt="image" src="https://user-images.githubusercontent.com/54215462/187186340-4def4e5a-1eba-49da-b38a-ea04dbc2a5a9.png">
<img width="323" alt="image" src="https://user-images.githubusercontent.com/54215462/187186394-35cf7a58-f6cc-4b29-9897-68d9812e8566.png">
<img width="397" alt="image" src="https://user-images.githubusercontent.com/54215462/187186725-99a2b6fd-f144-445d-a0ec-3ed6cda367b9.png">
<img width="301" alt="image" src="https://user-images.githubusercontent.com/54215462/187186768-4c21861b-252d-461d-bc56-7f5bbe145fac.png">
<img width="414" alt="image" src="https://user-images.githubusercontent.com/54215462/187186901-59a29819-255e-443d-b864-ae60577e05f2.png">
<img width="578" alt="image" src="https://user-images.githubusercontent.com/54215462/187187055-309e1870-86c1-4dd4-8284-7fc80baebf9b.png">


## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
