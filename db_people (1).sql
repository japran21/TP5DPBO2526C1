-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 17 Okt 2026 pada 16.14
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_people`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `person`
--

CREATE TABLE `person` (
  `id` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `thn_lahir` int(11) NOT NULL,
  `kategori` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `person`
--

INSERT INTO `person` (`id`, `nama`, `thn_lahir`, `kategori`, `email`) VALUES
('123', 'Ara malika Jasmine', 2007, 'Full-time', '1231123131'),
('2345647', 'cahyawa', 2009, 'Full-time', '23456532'),
('P000012', 'gibran', 1987, 'Part-time', 'Jamala@geming.Ik'),
('P001', 'Budi Santoso', 1988, 'Full-time', 'budi.santoso@example.com'),
('P002', 'Siti Rahmawati', 1995, 'Full-time', 'siti.rahmawati@example.com'),
('P003', 'Ahmad Rizky', 2002, 'Internship', 'ahmad.rizky@example.com'),
('P004', 'Dewi Lestari', 1985, 'Contract', 'dewi.lestari@example.com'),
('P005', 'Eko Prasetyo', 1998, 'Part-time', 'eko.prasetyo@example.com'),
('P006', 'Fitriani', 2003, 'Internship', 'fitriani@example.com'),
('P007', 'Gita Gutawa', 1992, 'Full-time', 'gita.gutawa@example.com'),
('P008', 'Hendra Wijaya', 1990, 'Contract', 'hendra.wijaya@example.com'),
('P010', 'Joko Widodo', 1987, 'Full-time', 'joko.widodo@example.com');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `person`
--
ALTER TABLE `person`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
