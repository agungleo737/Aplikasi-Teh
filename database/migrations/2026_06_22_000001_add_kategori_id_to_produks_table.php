<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        // 1. Tambah kolom FK
        Schema::table('produks', function (Blueprint $table) {
            $table->foreignId('kategori_id')->nullable()->after('kategori')
                  ->constrained('kategoris')->nullOnDelete();
        });

        if (Schema::hasColumn('produks', 'kategori')) {
            $namaList = DB::table('produks')
                ->whereNotNull('kategori')
                ->where('kategori', '!=', '')
                ->distinct()
                ->pluck('kategori');

            foreach ($namaList as $nama) {
                $kategoriId = DB::table('kategoris')->where('nama_kategori', $nama)->value('id');

                if (!$kategoriId) {
                    $kategoriId = DB::table('kategoris')->insertGetId([
                        'nama_kategori' => $nama,
                        'deskripsi'     => null,
                        'created_at'    => now(),
                        'updated_at'    => now(),
                    ]);
                }

                DB::table('produks')->where('kategori', $nama)->update(['kategori_id' => $kategoriId]);
            }

            // 3. Hapus kolom string lama
            Schema::table('produks', function (Blueprint $table) {
                $table->dropColumn('kategori');
            });
        }
    }

    public function down(): void
    {
        // Kembalikan kolom string
        Schema::table('produks', function (Blueprint $table) {
            $table->string('kategori')->nullable()->after('id');
        });

        $rows = DB::table('produks')
            ->join('kategoris', 'produks.kategori_id', '=', 'kategoris.id')
            ->select('produks.id', 'kategoris.nama_kategori')
            ->get();

        foreach ($rows as $row) {
            DB::table('produks')->where('id', $row->id)->update(['kategori' => $row->nama_kategori]);
        }

        Schema::table('produks', function (Blueprint $table) {
            $table->dropConstrainedForeignId('kategori_id');
        });
    }
};
